import React, {useState} from 'react';
import {Route, Routes} from "react-router-dom";
import Library from "./books/Library";
import Login from "./Login";

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
                    <Route path="/" exact element={<Library/>}/>
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