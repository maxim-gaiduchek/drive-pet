import {EditOutlined, FolderOutlined} from "@ant-design/icons";
import {Button, Input, List, Modal, Skeleton} from "antd";
import {useState} from "react";
import {updateFolder} from "../../../../Services/FolderService";

export function FolderListItem({folder, setFolderToParent, setItemsUpdated}) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [folderName, setFolderName] = useState(folder.name);

    const showModal = (e) => {
        e.stopPropagation();
        setFolderName(folder.name);
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setLoading(true);
        updateFolder(folder.id, folderName, folder.parentFolder)
            .then(folder => {
                setIsModalOpen(false);
                setLoading(false);
                setFolderName(folder.name);
                setItemsUpdated(true);
            })
            .catch((e) => {
                console.log(e)
                setLoading(false);
            })
    };
    const handleCancel = () => {
        if (!loading) {
            setFolderName(folder.name);
            setIsModalOpen(false);
        }
    };

    let actions = [];
    if (folder.userAccessType === 'OWNER') {
        actions.push(
            <EditOutlined onClick={showModal} style={{
                color: "#1677ff"
            }}/>
        )
    }

    return (
        <>
            <List.Item
                onClick={setFolderToParent}
                actions={actions}
            >
                <Skeleton avatar title={false} loading={false} active>
                    <List.Item.Meta
                        avatar={<FolderOutlined/>}
                        title={<a>{folder.name}</a>}
                        description={`By: ${folder.owner.firstName} ${folder.owner.lastName} (${folder.owner.email})`}
                    />
                </Skeleton>
            </List.Item>
            <Modal
                title={"Edit folder " + folder.name}
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
                <Input placeholder="Enter folder name" value={folderName} count={{max: 30}}
                       onChange={e => setFolderName(e.target.value)} disabled={loading}
                       status={folderName.length === 0 ? "error" : ""}/>
            </Modal>
        </>
    )
}
