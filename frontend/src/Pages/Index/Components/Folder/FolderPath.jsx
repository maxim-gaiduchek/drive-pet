import {Link} from "react-router-dom";
import {Divider, Dropdown, Flex} from "antd";
import {ArrowLeftOutlined, FolderOutlined, HomeOutlined, MinusOutlined} from "@ant-design/icons";
import {secondaryBackgroundColor} from "../../../../colors";
import {useEffect, useState} from "react";
import {getAllParentFolders} from "../../../../Services/FolderService";

export function FolderPath({style, folder}) {
    const [parentFolders, setParentFolders] = useState([]);

    useEffect(() => {
        if (!folder.id) {
            setParentFolders([]);
            return;
        }
        getAllParentFolders(folder.id)
            .then(parentFolders => {
                setParentFolders(parentFolders);
            })
            .catch(() => {
                setParentFolders([]);
            })
    }, [folder]);

    let parentFolderPath = "/drive";
    if (folder.parentFolder && folder.parentFolder.id) {
        parentFolderPath += "?parentFolderId=" + folder.parentFolder.id;
    }
    let parentFoldersDropdown = [
        {
            key: "main-folder",
            label: (
                <Link to={"/drive"}>
                    My Drive
                </Link>
            ),
            icon: <HomeOutlined/>,
        }
    ];
    parentFoldersDropdown.push(...parentFolders.map(parentFolder => {
        return {
            key: "folder-" + parentFolder.id,
            label: (
                <Link to={"/drive?parentFolderId=" + parentFolder.id}>
                    {parentFolder.name}
                </Link>
            ),
            icon: <FolderOutlined/>,
        }
    }));

    return (
        <Flex style={{
            ...style,
            flexDirection: "row",
            flexWrap: "nowrap",
            height: "20px",
            margin: "10px 0",
        }}>
            {
                parentFolders.length > 0
                    ? <>
                        <Link to={parentFolderPath} style={{color: "black"}}>
                            <ArrowLeftOutlined/> Back to parent folder
                        </Link>
                        <Divider type={"vertical"} style={{height: "100%", backgroundColor: secondaryBackgroundColor}}/>
                    </>
                    : <></>
            }
            <Link to={"/drive"} style={{color: "black"}}>
                <HomeOutlined/> My Drive
            </Link>
            {
                parentFolders.length > 3
                    ? <>
                        <MinusOutlined rotate={-75}/>
                        <Dropdown menu={{items: parentFoldersDropdown}}>
                            ...
                        </Dropdown>
                    </>
                    : <></>
            }
            {
                parentFolders.slice(-3).map(parentFolder => {
                    return (
                        <>
                            <MinusOutlined rotate={-75}/>
                            <Link to={"/drive?parentFolderId=" + parentFolder.id} style={{color: "black"}}>
                                {parentFolder.name}
                            </Link>
                        </>
                    )
                })
            }
        </Flex>
    )
}
