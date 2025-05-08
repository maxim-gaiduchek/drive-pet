import {getRequest, postRequest} from "./RequestService";
import {apiUrl, securityUrl} from "../config";
import {sha256} from "js-sha256";

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
