import React, { Component } from 'react';
import { Container, FormGroup, Form, Button, Label, Alert } from "reactstrap";
import { Link } from "react-router-dom";
import AppNav from './AppNav'


class Home extends React.Component {
    
    emptyItem = {
        city: "",
        state:""
  };
    
    constructor(props){
        super(props);

        this.state={
            all_States : [],
            selectedState : 'Colorado',
            isStateSelected : false,
            all_cities_within_state : [],
            all_cities: [],
            selectedCity:'',
            isLoading: true,
            inMaterials:this.emptyItem
        }

        this.handleChangeStates = this.handleChangeStates.bind(this);
    }

    async componentDidMount(){

        const LOCAL_URL = process.env.REACT_APP_LOCAL_URL;

        alert("LOCAL URL = " + LOCAL_URL);

        fetch('/opportunityReport/getAllStates', {
            method: 'GET',
            headers: {
             'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            this.setState({
                all_States : data
            })
        });


        // fetch('/opportunityReport/getAllCities' , {
        //     method: 'GET',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     }    
        // })
        // .then(response => response.json())
        // .then(data => {
        //     this.setState({
        //         all_cities : data,
        //         isLoading: false
        //     })
        // }) 

        fetch('/opportunityReport/getAllCitiesWithinState?' + new URLSearchParams({
            state: 'Colorado'
        }),{
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        })
        .then(resp => resp.json())
        .then(data => {
            this.setState({
                all_cities_within_state : data,
                isLoading : false
            })
        })

    }//End of componentDidMount()

    async handleChangeStates(event){
        alert("Changing the state value == " + event.target.value);
        const stateChangedValue = event.target.value;
        const encodeStateValue = encodeURIComponent(stateChangedValue);

        const LOCAL_URL = process.env.REACT_APP_LOCAL_URL;

        fetch('/opportunityReport/getAllCitiesWithinState?' + new URLSearchParams({
            state: stateChangedValue
        }),{
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        })
        .then(resp => resp.json())
        .then(data => {
            this.setState({
                all_cities_within_state : data,
                isLoading : false
            })
        })

        alert("after setting up the all cities within state: ");
    }

//Begining of the render()
    render() {
        <h2>Demographic Analysis</h2>
        const all_States  = this.state.all_States;
        const all_cities_within_state = this.state.all_cities_within_state;
        const isLoading = this.state.isLoading;


        //console.log("All States = " + all_States)
        let all_States_list = all_States.map(state => <option key={state.State_Name}>
            {state.State_Name}
        </option>)
        //console.log(all_States_list)

        let all_cities_list = all_cities_within_state.map(city => 
            <option key={city.City_Name}>
                {city.City_Name}
            </option>
        )

        //console.log("All Cities = " + all_cities)

        if (isLoading) return <div>Loading...</div>;

        return (

            <div class='main-div-top'>
                <AppNav />

                <div id='div-form-group'>
                    <Form id='form-main'>
                        <FormGroup>
                            <label style={{paddingRight: '12px'}}>States* </label>{ }
                            <select onChange={this.handleChangeStates}>
                                <option>Select State Name:</option>
                                {all_States_list}
                            </select>
                        </FormGroup>
                    

                        <FormGroup>
                            <label style={{paddingRight: '12px'}}>Cities* </label>{ }
                            <select onChange={this.handleChange}>
                                <option>Select City Name: </option>
                                {all_cities_list}
                            </select>
                        </FormGroup>
                        

                        <FormGroup>
                            <label style={{paddingRight: '12px'}}>Distance* </label>{ }
                            <select>
                                <option>30</option>
                                <option>35</option>
                                <option>40</option>
                                <option>45</option>
                                <option>50</option>
                                <option>55</option>
                                <option>60</option>
                            </select>
                        </FormGroup>
                            

                        <FormGroup>
                            <Button
                                color="primary"
                                type="submit"
                                //   onClick={this.onShowAlert}
                            >
                                {" "}
                                Save{" "}
                            </Button>{" "}
                            
                            <Button color="secondary">
                                {" "}
                                Cancel{" "}
                            </Button>
                    </FormGroup>

                    </Form>
                </div>
            </div>

        )
    }
}
 
export default Home;