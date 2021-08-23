from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)

"""
Data structures
"""
next_ride_id = 0
travel_driver_list = []
travel_passenger_list = []

"""
Helper Functions
"""


def get_rides_helper(start: str, end: str, date: str):
    rides = []
    for travel in travel_driver_list:
        if(travel['start'] == start and travel['end'] == end and travel['date'] == date):
            rides.append(travel)

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

    travel_passenger_list.append(
        {'id': next_ride_id, 'name': data['name'], telephone: data['telephone'], 'start': data['start'], 'end': data['end'], 'date': data['date']})

    #TODO Notify drivers
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

    travel_driver_list.append(
        {'id': next_ride_id, 'name': data['name'], telephone: data['telephone'], start': data['start'], 'end': data['end'], 'date': data['date'], 'number_passenger': data['number_passenger']})

        #TODO Notify passengers
    return jsonify({'id': next_ride_id})


@app.route("/cancel-passenger-interest", methods=['DELETE'])
def cancel_passenger_interest():
    ride_id = request.args.get('id')
    cancel_passenger_interest_helper(ride_id)
    return jsonify({'message': 'O evento ' + ride_id + ' foi cancelado'})
