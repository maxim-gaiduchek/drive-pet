import {List, Modal, Select, Skeleton} from "antd";
import {useEffect, useState} from "react";
import {getRequest} from "../../../Services/RequestService";
import {apiUrl} from "../../../config";
import {UserOutlined} from "@ant-design/icons";

const accessTypes = {
    "READ": {
        label: "Reader"
    },
    "READ_WRITE": {
        label: "Editor"
    },
}

export function UserAccessListModal({item, isModalOpen, setIsModalOpen}) {
    const [userAccesses, setUserAccesses] = useState([]);
    const [initLoading, setInitLoading] = useState(true);
    const type = item.type.toLowerCase();

    const handleCancel = (e) => {
        e.stopPropagation();
        setIsModalOpen(false);
    }

    const getAccessType = (userAccess) => {
        let accessType = userAccess.accessType;
        return accessType === "OWNER" ? "Owner" : accessType;
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
                    renderItem={userAccess => {
                        let title = `${userAccess.user.firstName} ${userAccess.user.lastName}`;
                        if (userAccess.user.id === +localStorage.getItem("loginUserId")) {
                            title += " (You)"
                        }
                        return (
                            <List.Item
                                onClick={e => e.stopPropagation()}
                                /*actions={actions}*/
                            >
                                <Skeleton avatar title={false} loading={false} active>
                                    <List.Item.Meta
                                        avatar={<UserOutlined/>}
                                        title={title}
                                        description={userAccess.user.email}
                                    />
                                    <Select placeholder={"Choose access type"} value={getAccessType(userAccess)}
                                            options={Object.entries(accessTypes).map(([key, value]) => {
                                                return {label: value.label, value: key}
                                            })}
                                            disabled={!isModalOpen || userAccess.user.id === item.owner.id}
                                            style={{width: "30%", margin: "10px 10px"}}/>
                                </Skeleton>
                            </List.Item>
                        )
                    }}
                />
            </Modal>
        </>
    )
}
