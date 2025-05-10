import {CloudDownloadOutlined, FileOutlined} from "@ant-design/icons";
import {Button, Flex, Image, List, Modal, Skeleton} from "antd";
import {useState} from "react";
import {Link} from "react-router-dom";
import ReactPlayer from 'react-player'

export function FileListItem({file}) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [muted, setMuted] = useState(false);

    const showModal = () => {
        setMuted(false);
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setMuted(false);
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setMuted(false);
        setIsModalOpen(false);
    };

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
            <ReactPlayer url={file.path} controls={true} muted={muted} width={"1200px"} height={"500px"}/>
        );
    } else {
        modalTitle = `File ${file.name}`;
        modalContent = (
            <Flex style={{ flexDirection: "column", alignItems: "center" }}>
                <FileOutlined style={{fontSize: "150px"}}/>
                <p style={{ fontSize: 16 }}>Size: {formatBytes(file.size)}</p>
                <p style={{ fontSize: 14 }}>Created by: {file.author.firstName} {file.author.lastName} ({file.author.email})</p>
            </Flex>
        );
    }

    return (
        <>
            <List.Item
                onClick={showModal}
                actions={[
                    <a key="list-loadmore-edit">edit</a>,
                    <a key="list-loadmore-more">more</a>
                ]}
            >
                <Skeleton avatar title={false} loading={false} active>
                    <List.Item.Meta
                        avatar={<FileOutlined/>}
                        title={<a>{file.name}</a>}
                        description={`Size: ${formatBytes(file.size)}, By: ${file.author.firstName} ${file.author.lastName} (${file.author.email})`}
                    />
                    <div>content</div>
                </Skeleton>
            </List.Item>
            <Modal
                title={modalTitle}
                closable={{"aria-label": "Custom Close Button"}}
                open={isModalOpen}
                onOk={handleOk}
                onCancel={handleCancel}
                footer={(_, {OkBtn}) => (
                    <>
                        <Link to={file.path} style={{margin: "0 10px", padding: 0}}>
                            <Button><CloudDownloadOutlined/> Download</Button>
                        </Link>
                        <OkBtn/>
                    </>
                )}
            style={{
            }}>
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
