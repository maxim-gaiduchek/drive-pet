import {FolderOutlined} from "@ant-design/icons";
import {Button, List} from "antd";

export function FolderListItem({folder, setParentFolderId}) {
    return (
        <Button onClick={() => setParentFolderId(folder.id)}>
            <List.Item.Meta
                avatar={<FolderOutlined/>}
                title={folder.name}
                /*description="Ant Design, a design language for background applications, is refined by Ant UED Team"*/
            />
        </Button>
    )
}
