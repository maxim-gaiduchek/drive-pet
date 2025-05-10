import {FileAddOutlined, InboxOutlined} from "@ant-design/icons";
import {Button, Modal} from "antd";
import {useEffect, useState} from "react";
import Dragger from "antd/es/upload/Dragger";
import {createFile} from "../../../../Services/FileService";

export function AddFileButtonModal({style, parentFolder, setItemsUpdated}) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [file, setFile] = useState(undefined);
    const [fileList, setFileList] = useState([]);
    const [fileUploaded, setFileUploaded] = useState(false);
    const [saveFile, setSaveFile] = useState(false);

    const showModal = () => {
        setFile(undefined);
        setFileUploaded(false);
        setFileList([]);
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setSaveFile(true);
    };
    const handleCancel = () => {
        if (!loading) {
            setIsModalOpen(false);
            setFile(undefined);
            setFileUploaded(false);
            setFileList([]);
        }
    };

    const uploadFile = (file) => {
        setFile(file);
        setFileUploaded(true);
        return false;
    };
    const onChange = (info) => {
        let newFileList = [...info.fileList];
        newFileList = newFileList.slice(-1);
        setFileList(newFileList);
    };
    const removeFile = () => {
        setFileUploaded(false);
    };

    useEffect(() => {
        if (!saveFile) {
            return;
        }
        setLoading(true);
        createFile(file, parentFolder)
            .then(() => {
                setIsModalOpen(false);
                setSaveFile(false);
                setLoading(false);
                setFile(undefined);
                setFileUploaded(false);
                setFileList([]);
                setItemsUpdated(true);
            })
            .catch(() => {
                setSaveFile(false);
                setLoading(false);
            })
    }, [saveFile]);

    return (
        <>
            <Button type="primary" icon={<FileAddOutlined/>} onClick={showModal}
                    style={{...style}}>
                Add File
            </Button>
            <Modal
                title={"Add new file in " + (parentFolder.id ? `${parentFolder.name} folder` : "My Drive")}
                closable={{"aria-label": "Custom Close Button"}}
                open={isModalOpen}
                onCancel={handleCancel}
                footer={(_, {OkBtn, CancelBtn}) => (
                    <>
                        <CancelBtn/>
                        <Button type={"primary"} loading={loading} onClick={handleOk}
                                disabled={!fileUploaded}>
                            Save
                        </Button>
                    </>
                )}>
                <Dragger name={"file"} beforeUpload={uploadFile} onChange={onChange} onRemove={removeFile}
                         fileList={fileList}>
                    <p className="ant-upload-drag-icon"><InboxOutlined/></p>
                    <p className="ant-upload-text">Click or drag file to this area to upload</p>
                    <p className="ant-upload-hint">Upload up to 1 file</p>
                </Dragger>
            </Modal>
        </>
    )
}
