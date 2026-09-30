import '../../styles/Footer.css'

function Footer() {
  return (
    <footer className="footer">
      <div className="footer__content">
        <p>&copy; {new Date().getFullYear()} TripSentinel. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
