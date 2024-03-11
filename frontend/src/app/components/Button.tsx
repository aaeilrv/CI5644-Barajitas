type ButtonProps = {
  link?: string;
  text: string;
  style?: React.CSSProperties;
  onClick?: () => void;
  color?: 'red' | 'green' | 'blue' | 'indigo'; // specify the colors you support
  testId?: string;
};

export default function Button({ link, text, style, onClick, color = 'indigo' ,testId}: ButtonProps) {
  const colorClasses = {
    red: 'bg-red-500 hover:bg-red-400 focus-visible:outline-red-500',
    green: 'bg-green-500 hover:bg-green-400 focus-visible:outline-green-500',
    blue: 'bg-blue-500 hover:bg-blue-400 focus-visible:outline-blue-500',
    indigo: 'bg-indigo-500 hover:bg-indigo-400 focus-visible:outline-indigo-500',
  };

  const colorClass = colorClasses[color];

  return (
    <button 
      data-testid={testId}
      style={style}
      onClick={onClick}
      className={`rounded px-2 py-2 text-sm font-semibold text-white shadow-sm ${colorClass}`}
    >
      {link ? <link href={link}>{text}</link> : text}
    </button>
  )
}