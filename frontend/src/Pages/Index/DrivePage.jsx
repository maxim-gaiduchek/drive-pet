import {MainLayout} from "../../Components/Layouts/MainLayout";
import {useEffect, useState} from "react";
import {getItems} from "../../Services/ItemService";
import {List} from "antd";
import {FileListItem} from "./Components/FileListItem";
import {FolderListItem} from "./Components/FolderListItem";
import {useNavigate, useSearchParams} from "react-router-dom";

export function DrivePage() {
    useEffect(() => {
        let loginUserId = localStorage.getItem("loginUserId");
        if (loginUserId === null) {
            navigate("/login");
        }
    }, []);

    const [parentFolder, setParentFolder] = useState({});

    document.title = `${parentFolder.name ? parentFolder.name : "My Drive"} | Drive Pet`

    const [searchParams, setSearchParams] = useSearchParams();
    const parentFolderId = searchParams.get("parentFolderId");
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(20);
    const [sortBy, setSortBy] = useState("createdAt");
    const [sortDirection, setSortDirection] = useState("desc");
    const [items, setItems] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        let queryParams = {
            sortBy: sortBy,
            sortDirection: sortDirection,
        };
        if (parentFolderId) {
            queryParams.parentFolderId = parentFolderId;
        }
        getItems(page, pageSize, queryParams)
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
                renderItem={item =>
                    item.type === 'FOLDER'
                        ? <FolderListItem folder={item}
                                          setFolderToParent={setFolderToParent(item, setParentFolder, setSearchParams)}/>
                        : <FileListItem file={item}/>
                }
            />
        </MainLayout>
    )
}

function setFolderToParent(folder, setParentFolder, setSearchParams) {
    return () => {
        setParentFolder(folder);
        setSearchParams({parentFolderId: folder.id})
    }
}
