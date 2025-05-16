import {Flex} from "antd";

export function CenteredLayout({children, style}) {
    return (
        <Flex style={{
            ...style,
            flexDirection: "column",
            alignItems: "center",
            width: "100%",
            maxWidth: 500,
            margin: "100px auto"
        }}>
            {children}
        </Flex>
    )
}
