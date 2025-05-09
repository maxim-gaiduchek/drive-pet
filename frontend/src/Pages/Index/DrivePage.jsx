import {MainLayout} from "../../Components/Layouts/MainLayout";
import {useEffect, useState} from "react";
import {getItems} from "../../Services/ItemService";
import {Button, Divider, List} from "antd";
import {FileListItem} from "./Components/FileListItem";
import {FolderListItem} from "./Components/FolderListItem";
import {useNavigate, useSearchParams} from "react-router-dom";

const PAGE_SIZE = 20;

export function DrivePage() {
    const navigate = useNavigate();
    const [parentFolder, setParentFolder] = useState({});

    document.title = `${parentFolder.name ? parentFolder.name : "My Drive"} | Drive Pet`

    const [loading, setLoading] = useState(false);
    const [searchParams, setSearchParams] = useSearchParams();
    const parentFolderId = searchParams.get("parentFolderId");
    const [nextPage, setNextPage] = useState(1);
    const [hasNext, setHasNext] = useState(true);
    const [sortBy, setSortBy] = useState("createdAt");
    const [sortDirection, setSortDirection] = useState("desc");
    const [items, setItems] = useState([]);
    const loadMoreData = () => {
        console.log(loading, hasNext);
        if (loading || !hasNext) {
            return;
        }
        setLoading(true);
        let queryParams = {
            sortBy: sortBy,
            sortDirection: sortDirection,
        };
        if (parentFolderId) {
            queryParams.parentFolderId = parentFolderId;
        }
        getItems(nextPage, PAGE_SIZE, queryParams)
            .then(responseItems => {
                setItems([...items, ...responseItems]);
                setNextPage(nextPage + 1);
                setHasNext(responseItems.length === PAGE_SIZE);
                setLoading(false);
            })
            .catch(() => {
                setLoading(false);
            })
    };
    useEffect(() => {
        let loginUserId = localStorage.getItem("loginUserId");
        if (loginUserId === null) {
            navigate("/login");
        }
    }, []);
    useEffect(() => {
        setNextPage(1);
        loadMoreData();
    }, [parentFolderId, sortBy, sortDirection]);
    const loadMore =
        !loading && hasNext ? (
            <div
                style={{
                    textAlign: 'center',
                    marginTop: 12,
                    height: 32,
                    lineHeight: '32px',
                }}
            >
                <Button onClick={loadMoreData}>loading more</Button>
            </div>
        ) : (
            <Divider>Found items: {items.length}</Divider>
        );
    return (
        <MainLayout>
            <List
                itemLayout="horizontal"
                dataSource={items}
                loadMore={loadMore}
                style={{
                    width: "100%",
                    margin: "10px 30px"
                }}
                renderItem={item =>
                    item.type === 'FOLDER'
                        ? <FolderListItem folder={item}
                                          setFolderToParent={() => setFolderToParent(item, setParentFolder, setSearchParams)}/>
                        : <FileListItem file={item}/>
                }
            />
        </MainLayout>
    )
}

function setFolderToParent(folder, setParentFolder, setSearchParams) {
    setParentFolder(folder);
    setSearchParams({parentFolderId: folder.id});
}
