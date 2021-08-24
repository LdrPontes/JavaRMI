const axios = require('axios');
const http = axios.create({
    baseURL: 'http://localhost:5000',
});
var EventSource = require("eventsource");

class UserImpl {
    constructor(user_name, telephone) {
        this.user_name = user_name;
        this.telephone = telephone;

        this.source = new EventSource("http://localhost:5000/stream");

        this.source.addEventListener(this.telephone + '/driver', function (event) {
            var data = JSON.parse(event.data);
            console.log("Notificação de Passageiro " + JSON.stringify(data.message));
            this.userTerminal()
        }, false);

        this.source.addEventListener(this.telephone + '/passenger', function (event) {
            var data = JSON.parse(event.data);
            console.log("Notificação de Carona " + JSON.stringify(data.message));
            this.userTerminal()
        }, false);
    }

    async setRideInterest(start, end, date) {
        const response = await http.post('/set-ride-interest', {
            name: this.user_name,
            telephone: this.telephone,
            start: start,
            end: end,
            date: date
        });
        console.log("\n\nSeu id de interesse de carona é: " + response.data['id']);
        this.userTerminal()

    }

    async cancelRideInterest(id) {
        const response = await http.delete('/cancel-ride-interest?id=' + id)
        console.log("\n\n" + response.data['message'])
        this.userTerminal()
    }

    async setPassengerInterest(start, end, date, passengerNumber) {
        const response = await http.post('/set-passenger-interest', {
            name: this.user_name,
            telephone: this.telephone,
            start: start,
            end: end,
            date: date,
            number_passenger: passengerNumber,
        })
        console.log("\n\nSeu id de interesse de carona é: " + response.data['id']);

        this.userTerminal()
    }

    async cancelPassengerInterest(id) {

        const response = await http.delete('/cancel-passenger-interest?id=' + id)
        console.log("\n\n" + response.data['message'])
        this.userTerminal()
    }

    async consultRide(start, end, date) {
        const response = await http.get('/get-rides?start=' + start + '&end=' + end + '&date=' + date)
        const payload = response.data.map((item) => ({
            name: item.name,
            telephone: item.telephone,
            start: item.start,
            end: item.end,
            date: item.date,
            number_passenger: item.number_passenger,
        }));
        console.log('\n\n' + JSON.stringify(payload));

        this.userTerminal()
    }

    userTerminal() {

        const prompt = require('prompt-sync')();

        console.log("\n\nEscolha uma opção: ");
        console.log("\t1 - Consultar caronas");
        console.log("\t2 - Registrar interesse em eventos de caronas");
        console.log("\t3 - Cancelar interesse em eventos de caronas");
        console.log("\t4 - Registrar interesse em eventos de passageiros");
        console.log("\t5 - Cancelar interesse em eventos de passageiros");
        console.log("\t6 - Sair \n");

        let nextOption = prompt();

        switch (nextOption) {
            case '1': {
                let start = prompt("Informe o destino de origem: ");
                let end = prompt("Informe o destino final: ");
                let date = prompt("Informe a data (dd/mm/yyyy): ");

                this.consultRide(start, end, date);
                break;
            }

            case '2':
                {
                    let start = prompt("Informe o destino de origem: ");
                    let end = prompt("Informe o destino final: ");
                    let date = prompt("Informe a data (dd/mm/yyyy): ");

                    this.setRideInterest(start, end, date);
                    break;
                }
            case '3': {
                let id = prompt("\n\nInforme o id do interesse para cancelar: ");

                this.cancelRideInterest(id);
                break;
            }

            case '4':
                {
                    let start = prompt("\n\nInforme o destino de origem: ");
                    let end = prompt("Informe o destino final: ");
                    let date = prompt("Informe a data (dd/mm/yyyy): ");
                    let passengerNumber = prompt("Informe o número de passageiros: ");

                    this.setPassengerInterest(start, end, date, passengerNumber);
                    break;
                }

            case '5':
                {
                    let id = prompt("\n\nInforme o id do interesse para cancelar: ");
                    this.cancelPassengerInterest(id);
                    break;
                }

            default:
                return;
        }

    }
}

function User() {

    const prompt = require('prompt-sync')();

    const name = prompt('Informe seu nome: ');

    const telephone = prompt('Informe seu telefone: ');

    new UserImpl(name, telephone).userTerminal();

}


User()
