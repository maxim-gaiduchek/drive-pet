import {Divider, List, Modal, Popconfirm, Select, Skeleton} from "antd";
import {useEffect, useState} from "react";
import {deleteRequest, getRequest, putRequest} from "../../../Services/RequestService";
import {apiUrl} from "../../../config";
import {DeleteOutlined, UserOutlined} from "@ant-design/icons";

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

    const handleOk = (e) => {
        e.stopPropagation();
        setIsModalOpen(false);
    }
    const handleCancel = (e) => {
        e.stopPropagation();
        setIsModalOpen(false);
    }

    const getAccessType = (userAccess) => {
        let accessType = userAccess.accessType;
        return accessType === "OWNER" ? "Owner" : accessType;
    }
    const handleAccessUpdate = (userId, accessType) => {
        putRequest(`${apiUrl}/${type}s/${item.id}/accesses/users/${userId}`, {
            accessType: accessType
        })
            .then((userAccess) => {
                const newUserAccesses = userAccesses.map(access => {
                    if (access.user.id === userAccess.user.id) {
                        return { ...access, accessType: userAccess.accessType };
                    }
                    return access;
                });
                setUserAccesses(newUserAccesses);
            })
    }
    const handleAccessDelete = (e, userId) => {
        e.stopPropagation();
        deleteRequest(`${apiUrl}/${type}s/${item.id}/accesses/users/${userId}`, false)
            .then(() => {
                let newUserAccesses = userAccesses.filter(access => access.user.id !== userId);
                setUserAccesses(newUserAccesses);
            })
    }

    useEffect(() => {
        getRequest(`${apiUrl}/${type}s/${item.id}/accesses`)
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
        <Modal
            title={"Accesses to " + item.name}
            closable={{"aria-label": "Custom Close Button"}}
            open={isModalOpen}
            onOk={handleOk}
            onCancel={handleCancel}
            style={{
                minWidth: "800px"
            }}>
            <Divider orientation="left">Users that have an access to the {type}</Divider>
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
                        >
                            <Skeleton avatar title={false} loading={false} active>
                                <List.Item.Meta
                                    avatar={<UserOutlined/>}
                                    title={title}
                                    description={userAccess.user.email}
                                />
                                {
                                    userAccess.user.id !== item.owner.id
                                        ? <Popconfirm
                                            title={`Remove an ${item.name} access`}
                                            description={`Are you sure to remove an access from ${userAccess.user.firstName} ${userAccess.user.lastName}?`}
                                            onConfirm={e => handleAccessDelete(e, userAccess.user.id)}
                                            onCancel={e => e.stopPropagation()}
                                            okText="Yes"
                                            cancelText="No"
                                        >
                                            <DeleteOutlined onClick={e => e.stopPropagation()}
                                                            style={{color: "#ff4d4f"}}/>
                                        </Popconfirm>
                                        : <></>
                                }
                                <Select placeholder={"Choose access type"} value={getAccessType(userAccess)}
                                        onSelect={value => handleAccessUpdate(userAccess.user.id, value)}
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
    )
}
