import {List, Modal, Skeleton} from "antd";
import {useEffect, useState} from "react";
import {getRequest} from "../../../Services/RequestService";
import {apiUrl} from "../../../config";
import {UserOutlined} from "@ant-design/icons";

export function UserAccessListModal({item, isModalOpen, setIsModalOpen}) {
    const [userAccesses, setUserAccesses] = useState([]);
    const [initLoading, setInitLoading] = useState(true);
    const type = item.type.toLowerCase();

    const handleCancel = (e) => {
        e.stopPropagation();
        setIsModalOpen(false);
    }

    useEffect(() => {
        getRequest(apiUrl + "/api/" + type + "s/" + item.id + "/accesses")
            .then(accesses => {
                setUserAccesses(accesses);
                setInitLoading(false);
            })
            .catch(() => {
                setUserAccesses([]);
                setInitLoading(false);
            })
    }, []);

    return (
        <>
            <Modal
                title={"Accesses for " + item.name}
                closable={{"aria-label": "Custom Close Button"}}
                open={isModalOpen}
                onCancel={handleCancel}
                footer={(_, {OkBtn, CancelBtn}) => (
                    <>
                        <CancelBtn/>
                        <OkBtn/>
                        {/*<Button type={"primary"} loading={loading} onClick={handleOk}
                                disabled={folderName.length === 0}>
                            Save
                        </Button>*/}
                    </>
                )}>
                <List
                    itemLayout="horizontal"
                    dataSource={userAccesses}
                    style={{
                        width: "90%",
                        margin: "10px 30px",
                    }}
                    loading={initLoading}
                    renderItem={item =>
                        <List.Item
                            /*actions={actions}*/
                        >
                            <Skeleton avatar title={false} loading={false} active>
                                <List.Item.Meta
                                    avatar={<UserOutlined/>}
                                    title={`${item.user.firstName} ${item.user.lastName}`}
                                    description={item.user.email}
                                />
                                <div>content</div>
                            </Skeleton>
                        </List.Item>
                    }
                />
            </Modal>
        </>
    )
}
