import {MainLayout} from "../../Components/Layouts/MainLayout";
import {useEffect, useState} from "react";
import {getItems} from "../../Services/ItemService";
import {Button, Divider, Flex, List, Select} from "antd";
import {FileListItem} from "./Components/FileListItem";
import {FolderListItem} from "./Components/FolderListItem";
import {useNavigate, useSearchParams} from "react-router-dom";
import Search from "antd/lib/input/Search";

const PAGE_SIZE = 20;
const sorts = {
    "newest": {
        label: "Newest",
        sortBy: "createdAt",
        sortDirection: "desc",
    },
    "oldest": {
        label: "Oldest",
        sortBy: "createdAt",
        sortDirection: "asc",
    },
    "name asc": {
        label: "Name (a -> z)",
        sortBy: "name",
        sortDirection: "asc",
    },
    "name desc": {
        label: "Name (z -> a)",
        sortBy: "name",
        sortDirection: "desc",
    },
}

export function DrivePage() {
    const navigate = useNavigate();
    const [parentFolder, setParentFolder] = useState({});

    document.title = `${parentFolder.name ? parentFolder.name : "My Drive"} | Drive Pet`

    const [loading, setLoading] = useState(false);
    const [searchParams, setSearchParams] = useSearchParams();
    const parentFolderId = searchParams.get("parentFolderId");
    const [name, setName] = useState("")
    const [nextPage, setNextPage] = useState(1);
    const [hasNext, setHasNext] = useState(true);
    const [sortBy, setSortBy] = useState("createdAt");
    const [sortDirection, setSortDirection] = useState("desc");
    const [items, setItems] = useState([]);
    const loadMoreData = (page = nextPage, isHasNext = hasNext) => {
        if (loading || !isHasNext) {
            return;
        }
        setLoading(true);
        let queryParams = {
            name: name,
            sortBy: sortBy,
            sortDirection: sortDirection,
        };
        if (parentFolderId) {
            queryParams.parentFolderId = parentFolderId;
        }
        getItems(page, PAGE_SIZE, queryParams)
            .then(responseItems => {
                if (page === 1) {
                    setItems(responseItems)
                } else {
                    setItems([...items, ...responseItems]);
                }
                setNextPage(page + 1);
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
        loadMoreData(1, true);
    }, [parentFolderId, name, sortBy, sortDirection]);
    const getSort = () => {
        let sort = Object.entries(sorts)
            .filter(([_, sort]) => sort.sortBy === sortBy && sort.sortDirection === sortDirection);
        return sort.length > 0 ? sort[0][0] : "newest"
    };
    const setSort = (value) => {
        const sort = sorts[value];
        setSortBy(sort.sortBy);
        setSortDirection(sort.sortDirection);
    };
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
                <Button onClick={() => loadMoreData()}>Load more</Button>
            </div>
        ) : (
            <Divider>Found items: {items.length}</Divider>
        );
    return (
        <MainLayout>
            <Flex style={{
                height: "100%",
                width: "80%",
                maxWidth: 1200,
                margin: "0 auto",
                justifyContent: "flex-start",
                flexWrap: "wrap",
                padding: "0 auto",
                overflowY: "auto",
                flexDirection: "column",
                alignItems: "center"
            }}>
                <Flex style={{
                    width: "90%",
                    alignItems: "center",
                    justifyContent: "space-between",
                    flexWrap: "nowrap",
                }}>
                    <Flex style={{
                        alignItems: "center",
                        justifyContent: "space-between",
                        flexWrap: "nowrap",
                        width: "1000%"
                    }}>
                        <Select placeholder={"Newest"} value={getSort()} onSelect={value => setSort(value)}
                                options={Object.entries(sorts).map(([key, value]) => {
                                    return {label: value.label, value: key}
                                })}
                                disabled={loading}
                                style={{width: "10%", margin: "10px 10px"}}/>
                        <Search placeholder={"Search name..."} value={name} allowClear={true}
                            onChange={e => setName(e.target.value)}
                            style={{width: "30%", margin: "10px 10px"}}/>
                    </Flex>
                </Flex>
                <List
                    itemLayout="horizontal"
                    dataSource={items}
                    loadMore={loadMore}
                    style={{
                        width: "90%",
                        margin: "10px 30px",
                    }}
                    renderItem={item =>
                        item.type === 'FOLDER'
                            ? <FolderListItem folder={item}
                                              setFolderToParent={() => setFolderToParent(item, setParentFolder, setSearchParams)}/>
                            : <FileListItem file={item}/>
                    }
                />
            </Flex>
        </MainLayout>
    )
}

function setFolderToParent(folder, setParentFolder, setSearchParams) {
    setParentFolder(folder);
    setSearchParams({parentFolderId: folder.id});
}
