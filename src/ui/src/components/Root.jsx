import React, {useState} from 'react';
import {Route, Routes} from "react-router-dom";
import Library from "./books/Library.jsx";
import Login from "./Login.jsx";

const Root = () => {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [userName, setUserName] = useState("");

    const handleLogin = (username) => {
        setUserName(username);
        setIsAuthenticated(true);
    }

    return (
        <div>
            {isAuthenticated ?
                <Routes>
                    <Route path="/" exact element={
                        <Library
                            username={userName}
                            isAuthenticated={isAuthenticated}
                        />}
                    />
                </Routes>
                :
                <Routes>
                    <Route path="/" exact element={
                        <Login
                            onLogin={handleLogin}
                        />
                    }/>
                </Routes>
            }
        </div>
    );
};

export default Root;