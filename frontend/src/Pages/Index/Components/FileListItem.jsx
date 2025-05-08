import {FileOutlined} from "@ant-design/icons";
import {List, Skeleton} from "antd";

export function FileListItem({file}) {
    return (
        <List.Item
            actions={[
                <a key="list-loadmore-edit">edit</a>,
                <a key="list-loadmore-more">more</a>
            ]}
        >
            <Skeleton avatar title={false} loading={false} active>
                <List.Item.Meta
                    avatar={<FileOutlined/>}
                    title={<a>{file.name}</a>}
                    description={`Size: ${formatBytes(file.size)}`}
                />
                <div>content</div>
            </Skeleton>
        </List.Item>
    )
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) {
        return '0 B';
    }
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const value = parseFloat((bytes / Math.pow(k, i)).toFixed(decimals));
    return `${value} ${sizes[i]}`;
}
