import * as React from 'react';
import { Navigation } from './navigation';
import { MainContent } from './mainContent';
import { LoggerV2 } from './loggerv2';

export function App() {
	return (
		<div>
			<Navigation />
			<MainContent />
			<LoggerV2 />
			{/* <Logger /> */} /*todo: remove this*/
		</div>
	);
}
