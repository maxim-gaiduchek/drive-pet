import {useNavigate, useParams} from "react-router-dom";
import {MainLayout} from "../../Components/Layouts/MainLayout";
import {CenteredLayout} from "../../Components/Layouts/CenteredLayout";
import {CheckOutlined, CloseOutlined, LoadingOutlined} from "@ant-design/icons";
import {useEffect, useState} from "react";
import {putRequest} from "../../Services/RequestService";
import {apiUrl} from "../../config";

export function UserAccessPage() {
    const {token} = useParams();
    const [loading, setLoading] = useState(true);
    const [success, setSuccess] = useState(true);
    const navigate = useNavigate();
    const type = "file";

    useEffect(() => {
        putRequest(`${apiUrl}/${type}s/accesses/${token}`)
            .then(item => {
                setSuccess(true);
                setLoading(false);
                if (item.parentFolder) {
                    navigate("/drive?parentFolderId=" + item.parentFolder.id)
                } else {
                    navigate("/drive")
                }
            })
            .catch(() => {
                setSuccess(false);
                setLoading(false);
            })
    }, []);

    return (
        <MainLayout>
            <CenteredLayout>
                {
                    loading
                        ? <LoadingOutlined/>
                        : <></>
                }
                {
                    success
                        ? <>
                            <CheckOutlined style={{fontSize: 60, color: "#52c41a"}}/>
                            <p style={{fontSize: 30}}>Done! Redirecting...</p>
                        </>
                        : <>
                            <CloseOutlined style={{fontSize: 60, color: "#ff4d4f"}}/>
                            <p style={{fontSize: 30}}>Error granting access to the source</p>
                        </>
                }
            </CenteredLayout>
        </MainLayout>
    )
}
