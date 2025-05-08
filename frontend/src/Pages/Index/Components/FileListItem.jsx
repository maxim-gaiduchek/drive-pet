import {FileOutlined} from "@ant-design/icons";
import {List} from "antd";

export function FileListItem({file}) {
    return (
        <List.Item.Meta
            avatar={<FileOutlined />}
            title={file.name}
            description={`Size: ${formatBytes(file.size)}`}
        />
    )
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 B';

    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    const value = parseFloat((bytes / Math.pow(k, i)).toFixed(decimals));
    return `${value} ${sizes[i]}`;
}
