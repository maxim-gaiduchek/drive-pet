import {useEffect, useState} from "react";
import {Button, Flex, Tooltip} from "antd";
import {Link, useNavigate} from "react-router-dom";
import {LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {LoginRegisterButtons} from "./LoginRegisterButtons";
import {getUser} from "../../../../../Services/UserService";
import {logout} from "../../../../../Services/AuthService";

export function LoginUserProfile() {
    const [user, setUser] = useState({});
    const loginUserId = localStorage.getItem("loginUserId");
    const navigate = useNavigate();
    useEffect(() => {
        if (!loginUserId) {
            return;
        }
        getUser(loginUserId)
            .then(user => setUser(user))
            .catch(() => localStorage.removeItem("loginUserId"))
    }, []);
    const logoutOnClick = async () => {
        logout()
            .then(() => {
                setUser({});
                console.log(user)
                navigate("/drive");
            })
    }
    return (user.id ?
            <>
                <Flex style={{
                    alignItems: "center",
                }}>
                    <Button type={"text"} onClick={logoutOnClick} style={{
                        margin: "0 5px"
                    }}>
                        <Tooltip title={"Logout"} placement={"bottom"}>
                            <LogoutOutlined style={{
                                transform: "rotate(180deg)"
                            }}/>
                        </Tooltip>
                    </Button>
                    <Button type={"text"} style={{
                        margin: "0 5px",
                    }}>
                        <UserOutlined style={{marginRight: "10px"}}/>
                        <p>{user.firstName} {user.lastName} ({user.email})</p>
                    </Button>
                </Flex>
            </> :
            <>
                <LoginRegisterButtons/>
            </>
    )
}
