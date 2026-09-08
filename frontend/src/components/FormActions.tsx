import { Button, Stack } from '@mui/material'

interface FormActionsProps {
  loading: boolean
  submitLabel?: string
}

export default function FormActions({
  loading,
  submitLabel = 'Save',
}: FormActionsProps) {
  return (
    <Stack direction="row" sx={{ justifyContent: 'flex-end' }}>
      <Button type="submit" variant="contained" disabled={loading}>
        {loading ? 'Saving…' : submitLabel}
      </Button>
    </Stack>
  )
}
