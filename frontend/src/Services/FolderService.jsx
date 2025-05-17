import {getRequest, postRequest, putRequest} from "./RequestService";
import {apiUrl} from "../config";

export async function getFolder(id) {
    return getRequest(apiUrl + "/folders/" + id)
}

export async function getAllParentFolders(id) {
    return getRequest(apiUrl + "/folders/" + id + "/parents")
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

export async function updateFolder(id, name, parentFolder) {
    let body = {
        name: name,
    }
    if (parentFolder && parentFolder.id) {
        body.parentFolder = {
            id: parentFolder.id
        };
    }
    return putRequest(apiUrl + "/folders/" + id, body);
}
