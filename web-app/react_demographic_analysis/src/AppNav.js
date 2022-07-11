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
import './Stylesheet/main.css';

class AppNav extends React.Component {
    
    render() {

        return (
            <div id='navbar-div-main'>
                    <Navbar id="navbar-main" expand="md">
                        <NavbarBrand id='navbar-main-title' href="/">Demographic Analysis Tool </NavbarBrand>
                    
                        <Nav id='navbar-ul-main' className="mr-auto" navbar>
                            <NavItem>
                                <NavLink className='navlink-main' href="/home/">Home</NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink className='navlink-main' href="/">Opportunity Report</NavLink>
                            </NavItem> 
                        </Nav>
                    </Navbar>
            </div>
        );

    }//end of render()
}//end of class
 
export default AppNav;

