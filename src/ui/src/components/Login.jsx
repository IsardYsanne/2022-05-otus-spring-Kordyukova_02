import React, {useState} from 'react';
import login from "../styles/login.module.scss";
import library from "../styles/library.module.scss";

const Login = (props) => {

    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const {
        onLogin
    } = props;

    const loginSubmit = (e) => {
        e.preventDefault();
        e.stopPropagation();

        fetch('/perform_login', {
            method: 'post',
            headers: {
                "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: 'username=' + userName + '&password=' + password
        }).then(response => {
            if (response.status === 200) {
                setIsAuthenticated(true);
                onLogin(userName);
            }
        });
    }

    const validate = (data) => {
        return !data;
    }

    return (
        <div className={login.loginContainer}>
            <p className={login.title}>Вход в систему</p>
            <form className={login.formWrapper} action={"/login"}>
                <label htmlFor={1} className={login.formLabel}>
                    Введите логин:
                </label>
                <input
                    id={'username'}
                    name={'username'}
                    className={login.inputForm}
                    type={"login"}
                    value={userName}
                    onChange={(e) => {
                        setUserName(e.target.value);
                    }}
                    pattern={"\\s*\\S+.*"}
                    required
                />
                <label className={login.formLabel}>
                    Введите пароль:
                </label>
                <input
                    id={'password'}
                    name={'password'}
                    type={"password"}
                    className={login.inputForm}
                    value={password}
                    onChange={(e) => {
                        setPassword(e.target.value);
                    }}
                />
                <div className={library.btn_container}>
                    <button
                        className={login.btnSubmit}
                        type={"submit"}
                        onClick={(e) => loginSubmit(e)}
                        disabled={validate(userName) || validate(password)}
                    >
                        Войти
                    </button>
                </div>
            </form>
        </div>
    );
};

export default Login;