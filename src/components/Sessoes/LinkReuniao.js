export default function LinkReuniao({ link }) {
  return (
    <p className="link-reuniao">
      <strong>Link da sessão:</strong>{' '}
      <a href={link} target="_blank" rel="noopener noreferrer">
        {link}
      </a>
    </p>
  );
}
