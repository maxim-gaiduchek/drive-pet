import {MainLayout} from "../../Components/Layouts/MainLayout";
import {useEffect, useState} from "react";
import {getItems} from "../../Services/ItemService";
import {Button, Divider, Flex, List, Select} from "antd";
import {FileListItem} from "./Components/File/FileListItem";
import {FolderListItem} from "./Components/Folder/FolderListItem";
import {useNavigate, useSearchParams} from "react-router-dom";
import Search from "antd/lib/input/Search";
import {AddFolderButtonModal} from "./Components/Folder/AddFolderButtonModal";
import {getFolder} from "../../Services/FolderService";
import {AddFileButtonModal} from "./Components/File/AddFileButtonModal";

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
const types = {
    "all": {
        label: "All types",
        values: ["FOLDER", "FILE"]
    },
    "folder": {
        label: "Folders",
        values: ["FOLDER"]
    },
    "file": {
        label: "Files",
        values: ["FILE"]
    },
}

export function DrivePage() {
    const navigate = useNavigate();
    const [parentFolder, setParentFolder] = useState({});

    document.title = `${parentFolder.name ? parentFolder.name : "My Drive"} | Drive Pet`

    const [loading, setLoading] = useState(false);
    const [searchParams, setSearchParams] = useSearchParams();
    const parentFolderIdParam = searchParams.get("parentFolderId");
    const [searchName, setSearchName] = useState("");
    const [itemTypes, setItemTypes] = useState(["FOLDER", "FILE"]);
    const [nextPage, setNextPage] = useState(1);
    const [hasNext, setHasNext] = useState(true);
    const [sortBy, setSortBy] = useState("createdAt");
    const [sortDirection, setSortDirection] = useState("desc");
    const [items, setItems] = useState([]);
    const [itemAdded, setItemAdded] = useState(false);

    const loadMoreData = (page = nextPage, isHasNext = hasNext) => {
        if (loading || !isHasNext) {
            return;
        }
        setLoading(true);
        let queryParams = {
            name: searchName,
            types: itemTypes,
            sortBy: sortBy,
            sortDirection: sortDirection,
        };
        if (parentFolderIdParam) {
            queryParams.parentFolderId = parentFolderIdParam;
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
        if (parentFolderIdParam) {
            getFolder(parentFolderIdParam)
                .then(folder => setParentFolder(folder))
                .catch(() => setParentFolder({}))
        } else {
            setParentFolder({})
        }
    }, [parentFolderIdParam]);
    useEffect(() => {
        loadMoreData(1, true);
    }, [parentFolderIdParam, searchName, itemTypes, sortBy, sortDirection]);
    useEffect(() => {
        if (itemAdded) {
            loadMoreData(1, true);
            setItemAdded(false);
        }
    }, [itemAdded]);

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

    const getType = () => {
        let type = Object.entries(types)
            .filter(([_, type]) => type.values === itemTypes);
        return type.length > 0 ? type[0][0] : "all"
    };
    const setType = (value) => {
        const type = types[value];
        setItemTypes(type.values);
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
                padding: "0 auto",
                overflowY: "auto",
                flexDirection: "column",
                alignItems: "center",
            }}>
                <Flex style={{
                    width: "90%",
                    alignItems: "center",
                    justifyContent: "space-between",
                }}>
                    <Flex style={{
                        alignItems: "center",
                        width: "70%",
                    }}>
                        <Select placeholder={"Newest"} value={getSort()} onSelect={value => setSort(value)}
                                options={Object.entries(sorts).map(([key, value]) => {
                                    return {label: value.label, value: key}
                                })}
                                disabled={loading}
                                style={{width: "20%", margin: "10px 10px"}}/>
                        <Search placeholder={"Search name..."} value={searchName} allowClear={true}
                                onChange={e => setSearchName(e.target.value)}
                                style={{width: "30%", margin: "10px 10px"}}/>
                        <Select placeholder={"All types"} value={getType()} onSelect={value => setType(value)}
                                options={Object.entries(types).map(([key, value]) => {
                                    return {label: value.label, value: key}
                                })}
                                disabled={loading}
                                style={{width: "20%", margin: "10px 10px"}}/>
                    </Flex>
                    <Flex style={{
                        alignItems: "center",
                        width: "20%",
                    }}>
                        <AddFileButtonModal style={{margin: "0 1.3%", width: "48%",}} parentFolder={parentFolder}
                                            setItemAdded={setItemAdded}/>
                        <AddFolderButtonModal style={{margin: "0 1.3%", width: "48%",}} parentFolder={parentFolder}
                                              setItemAdded={setItemAdded}/>
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
                            ? <FolderListItem folder={item} setFolderToParent={() =>
                                setFolderToParent(item, setParentFolder, setSearchParams, setSearchName, setItemTypes)}/>
                            : <FileListItem file={item}/>
                    }
                />
            </Flex>
        </MainLayout>
    )
}

function setFolderToParent(folder, setParentFolder, setSearchParams, setSearchName, setItemTypes) {
    setParentFolder(folder);
    setSearchParams({parentFolderId: folder.id});
    setSearchName("");
    setItemTypes([]);
}
