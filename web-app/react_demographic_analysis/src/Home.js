import React, { Component } from 'react';
import { Container, FormGroup, Form, Button, Label, Alert } from "reactstrap";

class Home extends React.Component {
    constructor(props){
        super(props);

        this.state={
            all_States : []
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
        
    }

    render() {
        <h2>Demographic Analysis</h2>
        const all_States  = this.state.all_States;
       
        console.log("All States = " + all_States)
        let all_States_list = all_States.map(state => <option key={state.State_Name}>
            {state.State_Name}
        </option>)
         console.log(all_States_list)

        return (

            <div>
                <Form>
                    <FormGroup>
                        <label>States* </label>{ }
                        <select>
                            <option></option>
                            {all_States_list}
                        </select>
                    </FormGroup>
                </Form>
            </div>

        )
    }
}
 
export default Home;