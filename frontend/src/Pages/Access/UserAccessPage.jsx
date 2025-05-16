import {useParams} from "react-router-dom";
import {MainLayout} from "../../Components/Layouts/MainLayout";
import {CenteredLayout} from "../../Components/Layouts/CenteredLayout";

export function UserAccessPage() {
    const {token} = useParams();

    return (
        <MainLayout>
            <CenteredLayout>
                Loading... {token}
            </CenteredLayout>
        </MainLayout>
    )
}
