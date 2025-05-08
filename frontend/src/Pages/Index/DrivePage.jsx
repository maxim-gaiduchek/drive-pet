import {MainLayout} from "../../Components/Layouts/MainLayout";
import {useEffect, useState} from "react";
import {getItems} from "../../Services/ItemService";
import {List, Skeleton} from "antd";
import {FileListItem} from "./Components/FileListItem";
import {FolderListItem} from "./Components/FolderListItem";
import {useNavigate} from "react-router-dom";

export function DrivePage() {
    document.title = "My Drive | Drive Pet"
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const parentFolderIdParam = urlParams.get("parentFolderId");
    const sortByParam = urlParams.get("sortBy");
    const sortDirectionParam = urlParams.get("sortDirection");
    // const [page, setPage] = useState(pageParam ? +pageParam : 1);
    // const [pageSize, setPageSize] = useState(pageSizeParam ? +pageSizeParam : 10);
    const [parentFolderId, setParentFolderId] = useState(parentFolderIdParam ? parentFolderIdParam : null);
    const [sortBy, setSortBy] = useState(sortByParam ? sortByParam : "createdAt");
    const [sortDirection, setSortDirection] = useState(sortDirectionParam ? sortDirectionParam : "desc");
    const [items, setItems] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        changePath(navigate, parentFolderId, sortBy, sortDirection/*, page, pageSize*/);
        let queryParams = {
            sortBy: sortBy,
            sortDirection: sortDirection,
        };
        if (parentFolderId) {
            queryParams = {
                parentFolderId: parentFolderId,
                ...queryParams
            }
        }
        getItems(/*page, pageSize*/ 1, 20, queryParams)
            .then(itemPage => {
                setItems(itemPage);
                // setPage(itemPage.currentPage);
                // setTotal(itemPage.totalMatches);
            })
            .catch(() => {
                setItems([]);
                // setPage(1);
            })
    }, [parentFolderId /*name, costMin, costMax, selectedFilters, sortBy, sortDirection, page, pageSize*/]);
    return (
        <MainLayout>
            <List
                /*loading={initLoading}*/
                itemLayout="horizontal"
                /*loadMore={loadMore}*/
                dataSource={items}
                style={{
                    width: "100%",
                    margin: "10px 30px"
                }}
                renderItem={item => (
                    <List.Item
                        actions={[<a key="list-loadmore-edit">edit</a>, <a key="list-loadmore-more">more</a>]}
                    >
                        <Skeleton avatar title={false} loading={item.loading} active>
                            {
                                item.type === 'FOLDER'
                                    ? <FolderListItem folder={item} setParentFolderId={setParentFolderId}/>
                                    : <FileListItem file={item}/>
                            }
                            <div>content</div>
                        </Skeleton>
                    </List.Item>
                )}
            />
        </MainLayout>
    )
}

function changePath(navigate, parentFolderId, sortBy, sortDirection, page, pageSize) {
    let params = new URLSearchParams();
    if (parentFolderId) {
        params.set("parentFolderId", parentFolderId);
    }
    if (sortBy) {
        params.set("sortBy", sortBy);
    }
    if (sortDirection) {
        params.set("sortDirection", sortDirection);
    }
    if (page) {
        params.set("page", page);
    }
    if (pageSize) {
        params.set("pageSize", pageSize);
    }
    const newUrl = `/drive?${params.toString()}`;
    navigate(newUrl);
}
