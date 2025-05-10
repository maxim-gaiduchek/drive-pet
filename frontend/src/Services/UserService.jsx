import {getRequest, postRequest} from "./RequestService";
import {apiUrl, securityUrl} from "../config";

export async function getUser(id) {
    return getRequest(apiUrl + "/users/" + id)
}

export async function registerUser(firstName, lastName, email, password) {
    return postRequest(securityUrl + "/register", {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password
    })
}
