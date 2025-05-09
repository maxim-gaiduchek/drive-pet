import {FolderAddOutlined} from "@ant-design/icons";
import {Button, Input, Modal} from "antd";
import {useEffect, useState} from "react";
import {createFolder} from "../../../../Services/FolderService";

export function AddFolderButtonModal({style, parentFolder, setItemAdded}) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [folderName, setFolderName] = useState("");
    const [saveFolder, setSaveFolder] = useState(false);

    const showModal = () => {
        setFolderName("");
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setSaveFolder(true);
    };
    const handleCancel = () => {
        if (!loading) {
            setFolderName("");
            setIsModalOpen(false);
        }
    };

    useEffect(() => {
        if (!saveFolder) {
            return;
        }
        setLoading(true);
        createFolder(folderName, parentFolder)
            .then(() => {
                setIsModalOpen(false);
                setSaveFolder(false);
                setLoading(false);
                setFolderName("");
                setItemAdded(true);
            })
            .catch(() => {
                setSaveFolder(false);
                setLoading(false);
            })
    }, [saveFolder]);

    return (
        <>
            <Button type="primary" icon={<FolderAddOutlined/>} onClick={showModal}
                    style={{...style}}>
                Add Folder
            </Button>
            <Modal
                title={"Add new folder in " + (parentFolder.id ? `${parentFolder.name} folder` : "My Drive")}
                closable={{"aria-label": "Custom Close Button"}}
                open={isModalOpen}
                onCancel={handleCancel}
                footer={(_, {OkBtn, CancelBtn}) => (
                    <>
                        <CancelBtn/>
                        <Button type={"primary"} loading={loading} onClick={handleOk}
                                disabled={folderName.length === 0}>
                            Save
                        </Button>
                    </>
                )}>
                <Input placeholder="Enter folder name" value={folderName}
                       onChange={e => setFolderName(e.target.value)} disabled={loading}
                       status={folderName.length === 0 ? "error" : ""}/>
            </Modal>
        </>
    )
}
