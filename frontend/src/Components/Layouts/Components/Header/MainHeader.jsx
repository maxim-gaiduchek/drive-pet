import {Flex} from "antd";
import {Header} from "antd/lib/layout/layout";
import {Link} from "react-router-dom"
import {secondaryBackgroundColor} from "../../../../colors";
import {LoginProfile} from "./Login/LoginProfile";

export function MainHeader({headerHeight}) {
    return (
        <Header style={{
            width: "100%",
            height: headerHeight ? headerHeight : 100,
            backgroundColor: secondaryBackgroundColor,
            fontSize: "16pt"
        }}>
            <Flex style={{
                justifyContent: "space-between",
                alignItems: "center"
            }}>
                <Link to={"/drive"} style={{color: "black", fontWeight: 500}}><p>Drive</p></Link>
                <LoginProfile/>
            </Flex>
        </Header>
    )
}
