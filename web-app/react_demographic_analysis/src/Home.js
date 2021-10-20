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
    }

    async componentDidMount(){

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


        fetch('/opportunityReport/getAllCities' , {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }    
        })
        .then(response => response.json())
        .then(data => {
            this.setState({
                all_cities : data,
                isLoading: false
            })
        }) 

    }

    render() {
        <h2>Demographic Analysis</h2>
        const all_States  = this.state.all_States;
        const all_cities = this.state.all_cities;
        const isLoading = this.state.isLoading;


        //console.log("All States = " + all_States)
        let all_States_list = all_States.map(state => <option key={state.State_Name}>
            {state.State_Name}
        </option>)
        //console.log(all_States_list)

        let all_cities_list = all_cities.map(city => 
            <option key={city.City_Name}>
                {city.City_Name}
            </option>
        )

        //console.log("All Cities = " + all_cities)

        if (isLoading) return <div>Loading...</div>;

        return (

            <div>
                <AppNav />
                <Form>
                    <FormGroup>
                        <label>States* </label>{ }
                        <select
                        onChange={this.handleChange}>
                            <option></option>
                            {all_States_list}
                        </select>
                    </FormGroup>

                    <FormGroup>
                        <label>Cities* </label>{ }
                        <select onChange={this.handleChange}>
                            <option></option>
                            {all_cities_list}
                        </select>
                    </FormGroup>

                    <FormGroup>
                        <label>Distance* </label>{ }
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

        )
    }
}
 
export default Home;