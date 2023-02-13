// @ts-ignore
import React from "react";
import { ThemeProvider } from "@material-ui/styles";
import {CssBaseline} from "@material-ui/core";
import {MainView} from './components/MainView';
import {withRouter} from 'react-router';
import lightTheme from "./style";


const App = () => {
	return (
		<ThemeProvider theme={lightTheme}>
			<CssBaseline />
				<MainView/>
		</ThemeProvider>
	);
};

export default withRouter(App);
