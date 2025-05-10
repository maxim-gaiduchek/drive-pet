import {LoginUserProfile} from "./LoginUserProfile";
import {LoginRegisterButtons} from "./LoginRegisterButtons";

export function LoginProfile() {
    let loginUserId = localStorage.getItem("loginUserId");
    if (loginUserId) {
        return (
            <LoginUserProfile/>
        )
    }
    return (
        <LoginRegisterButtons/>
    )
}
