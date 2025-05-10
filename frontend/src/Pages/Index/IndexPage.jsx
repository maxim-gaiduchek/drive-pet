import {Link} from "react-router-dom";
import {Button} from "antd";
import {CenteredLayout} from "../../Components/Layouts/CenteredLayout";

export function IndexPage() {
    document.title = "Welcome | Drive Pet"
    return (
        <CenteredLayout>
            <h1 style={{textAlign: "center", marginBottom: 0}}>Drive Pet</h1>
            <p style={{textAlign: "center"}}>Your easy file storage</p>
            <div style={{textAlign: "center", width: "100%"}}>
                <Link to={"/drive"}>
                    <Button>Go to My Drive</Button>
                </Link>
            </div>
        </CenteredLayout>
    )
}
