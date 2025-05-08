import {Layout} from "antd";
import {Content} from "antd/lib/layout/layout";
import {MainHeader} from "./Components/Header/MainHeader";
import {MainFooter} from "./Components/MainFooter";

export function MainLayout(props) {
    const headerHeight = 100;
    const footerHeight = 50;
    return (
        <Layout style={{height: "100dvh"}}>
            <MainHeader headerHeight={headerHeight}/>
            <Content style={{
                display: "flex",
                minHeight: `calc(100% - ${headerHeight}px - ${footerHeight}px)`,
                width: "100%",
                overflowY: "auto",
            }}>
                {props.children}
            </Content>
            <MainFooter footerHeight={footerHeight}/>
        </Layout>
    )
}
