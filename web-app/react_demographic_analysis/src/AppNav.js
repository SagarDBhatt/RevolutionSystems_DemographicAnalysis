import React, { Component } from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  NavbarText
} from 'reactstrap';
import './App.css'

class AppNav extends React.Component {
    
    render() {

        return (
            <div>
                    <Navbar color="light" light expand="md">
                        <NavbarBrand href="/">Demographic Analysis Tool </NavbarBrand>
                    
                        <Nav className="mr-auto" navbar>
                            <NavItem>
                            <   NavLink href="/home/">Home</NavLink>
                            </NavItem>
                            <NavItem>
                            <   NavLink href="/">Opportunity Report</NavLink>
                            </NavItem> 
                        </Nav>
                    </Navbar>
            </div>
        );

    }//end of render()
}//end of class
 
export default AppNav;

