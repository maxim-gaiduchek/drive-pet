import {FolderOutlined} from "@ant-design/icons";
import {List, Skeleton} from "antd";

export function FolderListItem({folder, setFolderToParent}) {
    return (
        <List.Item
            onClick={setFolderToParent}
            actions={[
                <a key="list-loadmore-edit">edit</a>,
                <a key="list-loadmore-more">more</a>
            ]}
        >
            <Skeleton avatar title={false} loading={false} active>
                <List.Item.Meta
                    avatar={<FolderOutlined/>}
                    title={<a>{folder.name}</a>}
                    description={`By: ${folder.author.firstName} ${folder.author.lastName} (${folder.author.email})`}
                />
            </Skeleton>
        </List.Item>
    )
}
