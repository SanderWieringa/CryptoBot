import React from 'react'
import auth from './auth'

export const Header = (props) => {
    return (
        <div>
            <h1>Protected React Router</h1>
            <button onClick={() => {
                auth.logout(() => {
                    props.history.push('/')
                })
            }}>Logout</button>
        </div>
    )
}