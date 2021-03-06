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
            selectedState : '',
            isStateSelected : false,
            all_cities_within_state : [],
            all_cities: [],
            selectedCity:'',
            isLoading: true,
            selectedDistance : 0,
            inMaterials:this.emptyItem
        }

        this.handleChangeStates = this.handleChangeStates.bind(this);
        this.buttonClicked = this.buttonClicked.bind(this);
        this.handleChangeCity = this.handleChangeCity.bind(this);
        this.handleChangeDistance = this.handleChangeDistance.bind(this);
    }

    async componentDidMount(){

        const LOCAL_URL = process.env.REACT_APP_LOCAL_URL;

        //alert("LOCAL URL = " + LOCAL_URL);

        fetch('/opportunityReport/getAllStates', {
            method: 'GET',
            headers: {
             'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            this.setState({
                all_States : data,
                isLoading : false
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

        // fetch('/opportunityReport/getAllCitiesWithinState?' + new URLSearchParams({
        //     state: 'Colorado'
        // }),{
        //     method: 'GET',
        //     headers: {
        //         'Content-Type' : 'application/json'
        //     }
        // })
        // .then(resp => resp.json())
        // .then(data => {
        //     this.setState({
        //         all_cities_within_state : data,
        //         isLoading : false
        //     })
        // })

    }//End of componentDidMount()

    handleChangeStates(event){
        // alert("Changing the state value == " + event.target.value);
        // const stateChangedValue = event.target.value;
        // const encodeStateValue = encodeURIComponent(stateChangedValue);

        // const LOCAL_URL = process.env.REACT_APP_LOCAL_URL;

        this.setState({
            selectedState : event.target.value
        })

        fetch('/opportunityReport/getAllCitiesWithinState?' + new URLSearchParams({
            state: event.target.value
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
    }//end of function

    handleChangeCity(event){
        this.setState({
            selectedCity : event.target.value
        })
    }

    handleChangeDistance(event){
        this.setState({
            selectedDistance : event.target.value
        })
    }

    buttonClicked(event){
        //alert("submit btn clicked!! ");
        alert("Selected state = " + this.state.selectedState + "\nSelected city = " + this.state.selectedCity
            + "\nSelected Distance = " + this.state.selectedDistance);
        
        fetch('/opportunityReport/getCitiesWithinState/withinMiles?' + new URLSearchParams({
            city : this.state.selectedCity,
            state: this.state.selectedState,
            distance : this.state.selectedDistance
        }),{
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        })
        .then(response => {
          response.blob().then(blob => {
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement('a');
            a.href = url;
            a.download = 'Opportunity_Report_' + this.state.selectedCity + "_" + this.state.selectedState + '.xlsx';
            a.click();
          });
          //window.location.href = response.url;
      });
        
        fetch('/opportunityReport/getCitiesWithinState/withinMiles?' + new URLSearchParams({
            city : this.state.selectedCity,
            state: this.state.selectedState,
            distance : this.state.selectedDistance
        }),{
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        })
        .then(resp => resp.json())
        .then(data => {
            console.log("Report Generated");
        })

        // fetch('/opportunityReport/getCitiesWithinState/withinMiles?', new URLSearchParams({
        //     city : this.state.selectedCity,
        //     state : this.state.selectedState,
        //     distance : this.state.selectedDistance
        // }),{
        //     method : 'GET',
        //     headers: {
        //         'Content-type': 'application/json'
        //     }
        // })
        // .then(resp => resp.json())
        // .then(data => {
        //     console.log("Report Generated");
        // })
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
                        <FormGroup style={{margin : '10px'}}>
                            <label style={{paddingRight: '12px'}}>States* </label>{ }
                            <select required onChange={this.handleChangeStates}>
                                {/* <option>Select State Name:</option> */}
                                <option></option>
                                {all_States_list}
                            </select>
                        </FormGroup>
                    

                        <FormGroup style={{margin : '10px'}}>
                            <label style={{paddingRight: '12px'}}>Cities* </label>{ }
                            <select required id='select_city_main' onChange={this.handleChangeCity}>
                                {/* <option>Select City Name: </option> */}
                                <option></option>
                                {all_cities_list}
                            </select>
                        </FormGroup>
                        

                        <FormGroup style={{margin : '10px'}}>
                            <label style={{paddingRight: '12px'}}>Distance* </label>{ }
                            <select required id='select_distance_main' onChange={this.handleChangeDistance}>
                                <option></option>
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
                                // type="submit"
                                onClick={this.buttonClicked}
                            >
                                {" "}
                                Generate Report{" "}
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