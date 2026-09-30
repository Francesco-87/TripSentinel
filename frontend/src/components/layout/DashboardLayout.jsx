import Footer from "./Footer";
import DashboardHeader from "./DashboardHeader";
import { Outlet } from "react-router-dom";

function DashboardLayout() {
    return (
        <div className="dashboard-layout">
            <DashboardHeader />
            <Outlet />
            <Footer />
        </div>
    );
}

export default DashboardLayout;