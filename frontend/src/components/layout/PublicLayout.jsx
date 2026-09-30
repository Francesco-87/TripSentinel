import {Outlet} from 'react-router-dom'

import Header from './Header';
import Footer from './Footer';
  



function PublicLayout() {
  return (
    <div className="public-layout">
      <Header />      
      <Outlet />
      <Footer />
    </div>
  );
}

export default PublicLayout;