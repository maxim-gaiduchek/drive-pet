import {createBrowserRouter} from "react-router-dom";
import {IndexPage} from "../Pages/Index/IndexPage";
import {DrivePage} from "../Pages/Index/DrivePage";
import {LoginPage} from "../Pages/Login/LoginPage";
import {RegistrationPage} from "../Pages/Login/RegistrationPage";
import {UserAccessPage} from "../Pages/Access/UserAccessPage";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <IndexPage/>
    },
    {
        path: "/drive",
        element: <DrivePage/>
    },
    {
        path: "/login",
        element: <LoginPage/>
    },
    {
        path: "/register",
        element: <RegistrationPage/>
    },
    {
        path: "/drive/access/:token",
        element: <UserAccessPage/>
    }
])
