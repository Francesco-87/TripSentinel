
import {Link} from "react-router-dom"
import '../../styles/Header.css'


function Header() {
   

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
                        <a href="/#how">How It Works</a>
                    </li>
                    <li>
                        <a href="/#about">About</a>
                    </li>
                    <li>
                        <Link to="/login">Login</Link>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default Header;
