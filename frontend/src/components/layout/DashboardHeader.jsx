import {Link} from "react-router-dom"

import '../../styles/Header.css'

function DashboardHeader() {

        return (
        <header className="header">
            <div className="header__logo">
                <h1>
                    <Link to="/">TripSentinel</Link>
                </h1>
            </div>
            <nav className="header__nav">
                <ul>
                   
                    <li>
                        <Link to="/login">Logout</Link>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default DashboardHeader;