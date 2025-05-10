import {getRequest} from "./RequestService";
import {apiUrl} from "../config";

export async function getItems(page = 1, pageSize = 10, queryParams = {}) {
    return getRequest(apiUrl + "/items", true, {
        ...queryParams,
        page: page,
        pageSize: pageSize,
    });
}
