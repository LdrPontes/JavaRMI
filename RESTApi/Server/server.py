from flask import Flask
from flask import request
from flask import jsonify
from flask_sse import sse
from apscheduler.schedulers.background import BackgroundScheduler
import json

app = Flask(__name__)
app.config["REDIS_URL"] = "redis://localhost"
app.register_blueprint(sse, url_prefix='/stream')

"""
Data structures
"""
next_ride_id = 0
travel_driver_list = []
travel_passenger_list = []

"""
Helper Functions
"""


def notify_drivers(new_travel):
    if(new_travel == None):
        return

    for travel in travel_driver_list:
        if(travel['start'] == new_travel['start'] and travel['end'] == new_travel['end'] and travel['date'] == new_travel['date']):
            with app.app_context():
                sse.publish(json.dumps(new_travel),
                            type=travel['telephone'] + '/driver')


def notify_passengers(new_travel):

    if(new_travel == None):
        return

    for travel in travel_passenger_list:
        if(travel['start'] == new_travel['start'] and travel['end'] == new_travel['end'] and travel['date'] == new_travel['date']):
            with app.app_context():
                sse.publish(json.dumps(new_travel),
                            type=travel['telephone'] + '/passenger')


def get_rides_helper(start: str, end: str, date: str):
    rides = []
    for travel in travel_driver_list:
        if(travel['start'] == start and travel['end'] == end and travel['date'] == date):
            rides.append(travel)
    print(rides)
    return rides


def cancel_passenger_interest_helper(id: int):
    for i in range(len(travel_driver_list)):
        if travel_driver_list[i]['id'] == id:
            del travel_driver_list[i]
            break


def cancel_ride_interest_helper(id: int):
    for i in range(len(travel_passenger_list)):
        if travel_passenger_list[i]['id'] == id:
            del travel_passenger_list[i]
            break


"""
Routes
"""


@app.route("/get-rides", methods=['GET'])
def get_rides():
    start = request.args.get('start')
    end = request.args.get('end')
    date = request.args.get('date')
    return jsonify(get_rides_helper(start, end, date))


@app.route("/set-ride-interest", methods=['POST'])
def set_ride_interest():
    global next_ride_id
    next_ride_id = next_ride_id + 1
    data = request.get_json()

    new_travel = {'id': next_ride_id, 'name': data['name'], 'telephone': data['telephone'],
                  'start': data['start'], 'end': data['end'], 'date': data['date']}

    travel_passenger_list.append(new_travel)

    notify_drivers(new_travel)
    return jsonify({'id': next_ride_id})


@app.route("/cancel-ride-interest", methods=['DELETE'])
def cancel_ride_interest():
    ride_id = request.args.get('id')
    cancel_passenger_interest_helper(ride_id)
    return jsonify({'message': 'O evento ' + ride_id + ' foi cancelado'})


@app.route("/set-passenger-interest", methods=['POST'])
def set_passenger_interest():
    global next_ride_id
    next_ride_id = next_ride_id + 1
    data = request.get_json()

    new_travel = {'id': next_ride_id, 'name': data['name'], 'telephone': data['telephone'], 'start': data['start'],
                  'end': data['end'], 'date': data['date'], 'number_passenger': data['number_passenger']}
    travel_driver_list.append(new_travel)

    notify_passengers(new_travel)
    return jsonify({'id': next_ride_id})


@app.route("/cancel-passenger-interest", methods=['DELETE'])
def cancel_passenger_interest():
    ride_id = request.args.get('id')
    cancel_passenger_interest_helper(ride_id)
    return jsonify({'message': 'O evento ' + ride_id + ' foi cancelado'})
