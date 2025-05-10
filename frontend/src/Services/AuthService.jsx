import {securityUrl} from "../config";
import {postRequest} from "./RequestService";

export async function login(login, password) {
    return postRequest(securityUrl + "/login", {
        login: login,
        password: password
    })
        .then(credentials => {
            console.log(credentials)
            localStorage.setItem("loginUserId", credentials.userId);
            localStorage.setItem("loginUserRole", credentials.role);
            localStorage.setItem("loginAccessToken", credentials.accessToken);
            localStorage.setItem("loginRefreshToken", credentials.refreshToken);
            localStorage.setItem("loginTokenType", credentials.type);
        })
}

export async function logout() {
    /*return getRequest(securityUrl + "/logout", false)
        .then(() => {
            localStorage.removeItem("loginUserId");
            localStorage.removeItem("loginUserRole");
            localStorage.removeItem("loginAccessToken");
            localStorage.removeItem("loginRefreshToken");
            localStorage.removeItem("loginTokenType");
        })*/
    return new Promise((resolve) => {
        localStorage.removeItem("loginUserId");
        localStorage.removeItem("loginUserRole");
        localStorage.removeItem("loginAccessToken");
        localStorage.removeItem("loginRefreshToken");
        localStorage.removeItem("loginTokenType");
        resolve();
    })
}
