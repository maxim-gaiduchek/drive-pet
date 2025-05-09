import {getRequest, postRequest} from "./RequestService";
import {apiUrl} from "../config";

export async function getFolder(id) {
    return getRequest(apiUrl + "/folders/" + id)
}

export async function createFolder(name, parentFolder) {
    let body = {
        name: name,
    }
    if (parentFolder.id) {
        body.parentFolder = {
            id: parentFolder.id
        };
    }
    return postRequest(apiUrl + "/folders", body);
}
