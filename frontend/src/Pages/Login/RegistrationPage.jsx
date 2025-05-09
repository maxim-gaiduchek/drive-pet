import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {Input} from "antd";
import {MainLayout} from "../../Components/Layouts/MainLayout";
import {CenteredLayout} from "../../Components/Layouts/CenteredLayout";
import {login} from "../../Services/AuthService";
import {SubmitButton} from "../../Components/Form/Buttons/SubmitButton";
import {registerUser} from "../../Services/UserService";

export function RegistrationPage() {
    document.title = "Registration | Drive Pet";
    const navigate = useNavigate();
    const [userFirstName, setUserFirstName] = useState("");
    const [userLastName, setUserLastName] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [disabled, setDisabled] = useState(true);
    useEffect(() => {
        let loginUserId = localStorage.getItem("loginUserId");
        if (loginUserId) {
            navigate("/drive");
        }
    }, []);
    const loginAfterRegistration = async () => {
        setDisabled(true);
        login(userEmail, userPassword)
            .then(() => {
                navigate("/drive");
            })
            .catch(() => {
                setDisabled(false);
            });
    }
    const registerOnClick = async (e) => {
        e.preventDefault();
        setDisabled(true);
        registerUser(userFirstName, userLastName, userEmail, userPassword)
            .then(() => loginAfterRegistration())
            .catch(() => {
                setDisabled(false);
            });
    }
    const setupData = (value, setter) => {
        setter(value);
        setDisabled(!userFirstName || !userLastName || !userEmail || !userPassword);
    }
    return (
        <MainLayout>
            <CenteredLayout>
                <form onSubmit={(e) => registerOnClick(e)} style={{textAlign: "center"}}>
                    <h1>Registration</h1>
                    <p>Already have an account? <Link to={"/login"}>Login</Link></p>
                    <Input type={"text"} placeholder={"First Name"}
                           value={userFirstName}
                           onChange={(e) => setupData(e.target.value, setUserFirstName)}
                           style={{width: "100%", margin: "10px 10px"}}/>
                    <Input type={"text"} placeholder={"Last Name"}
                           value={userLastName}
                           onChange={(e) => setupData(e.target.value, setUserLastName)}
                           style={{width: "100%", margin: "10px 10px"}}/>
                    <Input type={"email"} placeholder={"Email"}
                           value={userEmail}
                           onChange={(e) => setupData(e.target.value, setUserEmail)}
                           style={{width: "100%", margin: "10px 10px"}}/>
                    <Input type={"password"} placeholder={"Password"}
                           value={userPassword}
                           onChange={(e) => setupData(e.target.value, setUserPassword)}
                           style={{width: "100%", margin: "10px 10px"}}/>
                    <SubmitButton disabled={disabled} value={"Register"}/>
                </form>
            </CenteredLayout>
        </MainLayout>
    )
}
