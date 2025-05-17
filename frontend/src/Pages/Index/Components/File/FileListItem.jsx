import {CloudDownloadOutlined, DeleteOutlined, EditOutlined, FileOutlined, UserOutlined} from "@ant-design/icons";
import {Button, Flex, Image, Input, List, Modal, Popconfirm, Skeleton} from "antd";
import {useState} from "react";
import {Link} from "react-router-dom";
import ReactPlayer from 'react-player'
import {deleteFile, updateFile} from "../../../../Services/FileService";
import {UserAccessListModal} from "../UserAccessListModal";
import {format} from 'date-fns';

export function FileListItem({file, setItemsUpdated}) {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editLoading, setEditLoading] = useState(false);
    const [fileName, setFileName] = useState(file.name);

    const [isAccessesModalOpen, setIsAccessesModalOpen] = useState(false);
    const [isUserAccessEditOpen, setIsUserAccessEditOpen] = useState(false);

    const showEditModal = (e) => {
        e.stopPropagation()
        setIsEditModalOpen(true);
        setFileName(file.name);
    };
    const handleEditOk = (e) => {
        e.stopPropagation();
        setEditLoading(true);
        updateFile(file.id, fileName, file.parentFolder)
            .then(file => {
                setIsEditModalOpen(false);
                setEditLoading(false);
                setFileName(file.fileName);
                setItemsUpdated(true);
            })
            .catch(() => {
                setEditLoading(false);
            })
    };
    const handleEditCancel = (e) => {
        e.stopPropagation()
        setIsEditModalOpen(false);
    };

    const showAccessesModal = () => {
        setIsAccessesModalOpen(true);
    };
    const handleAccessesOk = () => {
        setIsAccessesModalOpen(false);
    };
    const handleAccessesCancel = () => {
        setIsAccessesModalOpen(false);
    };

    const handleDelete = (e) => {
        e.stopPropagation();
        return deleteFile(file.id)
            .then(() => {
                setItemsUpdated(true);
            });
    }

    const handleUserAccessEditOpen = (e) => {
        e.stopPropagation();
        setIsUserAccessEditOpen(true);
    }

    let fileType = file.fileType.split("/")[0];
    let modalTitle;
    let modalContent;
    if (fileType === "image") {
        modalTitle = `Image ${file.name}`;
        modalContent = (
            <Image src={file.path} preview={false} style={{
                height: "100%",
            }}/>
        );
    } else if (fileType === "video") {
        modalTitle = `Video ${file.name}`;
        modalContent = (
            <ReactPlayer url={file.path} controls={true} width={"1200px"} height={"500px"}/>
        );
    } else {
        modalTitle = `File ${file.name}`;
        modalContent = (
            <Flex style={{flexDirection: "column", alignItems: "center"}}>
                <FileOutlined style={{fontSize: "150px"}}/>
                <p style={{fontSize: 16}}>Size: {formatBytes(file.size)}</p>
                <p style={{fontSize: 14}}>Created
                    by: {file.owner.firstName} {file.owner.lastName} ({file.owner.email})</p>
            </Flex>
        );
    }
    let actions = [];
    if (file.userAccessType === 'OWNER' || file.userAccessType === 'READ_WRITE') {
        actions.push(
            <EditOutlined onClick={showEditModal} style={{color: "#1677ff"}}/>
        )
    }
    if (file.userAccessType === 'OWNER') {
        actions.push(
            <>
                <UserOutlined onClick={handleUserAccessEditOpen} style={{color: "#1677ff"}}/>
                <UserAccessListModal item={file} isModalOpen={isUserAccessEditOpen}
                                     setIsModalOpen={setIsUserAccessEditOpen}/>
            </>,
            <Popconfirm
                title={"Delete file " + file.name}
                description={"Are you sure to delete this file?"}
                onConfirm={handleDelete}
                onCancel={e => e.stopPropagation()}
                okText="Yes"
                cancelText="No"
            >
                <DeleteOutlined onClick={e => e.stopPropagation()} style={{
                    color: "#ff4d4f"
                }}/>
            </Popconfirm>
        )
    }

    return (
        <>
            <List.Item
                onClick={showAccessesModal}
                actions={actions}
            >
                <Skeleton avatar title={false} loading={false} active>
                    <List.Item.Meta
                        avatar={<FileOutlined/>}
                        title={<a>{file.name}</a>}
                        description={`By: ${file.owner.firstName} ${file.owner.lastName} (${file.owner.email}), Size: ${formatBytes(file.size)}, Created: ${formatDate(file.createdAt)}`}
                    />
                </Skeleton>
            </List.Item>
            <Modal
                title={modalTitle}
                closable={{"aria-label": "Custom Close Button"}}
                open={isAccessesModalOpen}
                onOk={handleAccessesOk}
                onCancel={handleAccessesCancel}
                footer={(_, {OkBtn}) => (
                    <>
                        <Link to={file.path} style={{margin: "0 10px", padding: 0}}>
                            <Button><CloudDownloadOutlined/> Download</Button>
                        </Link>
                        <OkBtn/>
                    </>
                )}>
                <Flex style={{
                    maxWidth: "1000px",
                    minHeight: "300px",
                    alignItems: "center",
                    justifyContent: "center",
                    padding: "10px 10px",
                }}>
                    {modalContent}
                </Flex>
            </Modal>
            <Modal
                title={"Edit file " + file.name}
                closable={{"aria-label": "Custom Close Button"}}
                open={isEditModalOpen}
                onCancel={handleEditCancel}
                footer={(_, {OkBtn, CancelBtn}) => (
                    <>
                        <CancelBtn/>
                        <Button type={"primary"} loading={editLoading} onClick={handleEditOk}
                                disabled={fileName.length === 0}>
                            Save
                        </Button>
                    </>
                )}
            >
                <Input placeholder="Enter file name" value={fileName}
                       onClick={e => e.stopPropagation()}
                       onChange={e => {
                           e.stopPropagation();
                           setFileName(e.target.value);
                       }} disabled={editLoading}
                       status={fileName.length === 0 ? "error" : ""}/>
            </Modal>
        </>
    )
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) {
        return '0 B';
    }
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const value = parseFloat((bytes / Math.pow(k, i)).toFixed(decimals));
    return `${value} ${sizes[i]}`;
}

function formatDate(isoDate) {
    return format(new Date(isoDate), 'dd.MM.yyyy HH:mm');
}
