import {deleteRequest, getRequest, postRequestFormData, putRequest} from "./RequestService";
import {apiUrl} from "../config";

export async function getFile(id) {
    return getRequest(apiUrl + "/files/" + id)
}

export async function createFile(file, parentFolder) {
    let body = {}
    if (parentFolder.id) {
        body.parentFolder = {
            id: parentFolder.id
        };
    }
    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(body)], {type: "application/json"}));
    formData.append("file", file);
    return postRequestFormData(apiUrl + "/files", formData);
}

export async function updateFile(id, fileName, parentFolder) {
    let body = {
        fileName: fileName,
    }
    if (parentFolder && parentFolder.id) {
        body.parentFolder = {
            id: parentFolder.id
        };
    }
    return putRequest(apiUrl + "/files/" + id, body);
}

export async function deleteFile(id) {
    return deleteRequest(apiUrl + "/files/" + id, false)
}
